/** 
 * Parsing TestNG Results report (target/surefire-reports/testng-results.xml)
 */

"use strict";

const fs = require("fs");
const globby = require("globby");
const xml2js = require("xml2js");
const path = require("path");
const archiver = require("archiver");
const os = require("os");

const MAX_LOG_FILE = 5;
const timestamp = new Date();
let resultMap = new Map();
let order = 0;
let useTestCase = false;
let classStatus = true;


// delete folder  recursively
function deleteFolderSync(folderPath) {
  if (fs.existsSync(folderPath)) {
    fs.readdirSync(folderPath).forEach(function (file, index) {
      var curPath = folderPath + "/" + file;
      if (fs.lstatSync(curPath).isDirectory()) {
        deleteFolderSync(curPath);
      } else { // delete file
        fs.unlinkSync(curPath);
      }
    });
    fs.rmdirSync(folderPath);
  }
};

function zipFolder(folderPath, filePattern, outputFilePath) {
  return new Promise(function (resolve, reject) {
    var output = fs.createWriteStream(outputFilePath);
    var zipArchive = archiver('zip');

    output.on('close', function () {
      console.log(zipArchive.pointer() + ' total bytes');
      console.log('archiver has been finalized and the output file descriptor has closed.');
      resolve(undefined);
    });

    output.on('end', function () {
      console.log('Data has been drained');
      resolve(undefined);
    });

    zipArchive.on('warning', function (err) {
      if (err.code === 'ENOENT') {
        // log warning
      } else {
        reject(err);
      }
    });

    zipArchive.on('error', function (err) {
      reject(err);
    });

    // pipe archive data to the file
    zipArchive.pipe(output);

    zipArchive.glob(filePattern, {
      cwd: folderPath
    });
    zipArchive.finalize(function (err, bytes) {
      if (err) {
        reject(err);
      }
    });
  })

}

function buildResultByTestMethod(obj, suite) {
  if (!obj || !obj.$ || !obj.$.name) {
    return undefined;
  }
  let className = obj.$.name;
  let testMethods = Array.isArray(obj['test-method']) ? obj['test-method'] : [obj['test-method']];
  for (let tm of testMethods) {
    let methodName = tm.$.name;
    let methodStatus = tm.$.status;
    let startTime = tm.$['started-at'];
    let endTime = tm.$['finished-at'];
    let note = '';
    let stack = '';
    if (methodStatus == 'FAIL') {
      note = tm.exception.message;
      stack = tm.exception['full-stacktrace'];

    }
    let testLog = {
      status: methodStatus,
      name: methodName,
      attachments: [],
      note: note,
      exe_start_date: startTime,
      exe_end_date: endTime,
      automation_content: className + "#" + methodName,
      module_names: [suite, className, methodName]
    };
    if (stack != '') {
      testLog.attachments.push({
        name: `${methodName}.txt`,
        data: Buffer.from(stack).toString("base64"),
        content_type: "text/plain"
      });
    }
    if (testLog && !resultMap.has(testLog.automation_content)) {
      testLog.order = order++;
      resultMap.set(testLog.automation_content, testLog);
    }
  }
}

function buildResultsBySuite(obj) {
  let suite = obj.$.name;
  let testClasses = Array.isArray(obj.class) ? obj.class : [obj.class];
  for (let tc of testClasses) {
  	if (useTestCase) {
  		buildTestResultByClassName(tc, suite);
  	} else {
    buildResultByTestMethod(tc, suite);
	}
  }
}

async function buildTestResultByClassName(obj, suite) {
  if (!obj || !obj.$ || !obj.$.name) {
    return undefined;
  }
  let buildAttachmentAndTestStepLogs = async (testcases) => {
    let attachments = [];
    let testStepLogs = [];
    let fileMap = new Map();
    let order = 0;
    classStatus = true;
    for (let tc of testcases) {
      if (tc) {
        testStepLogs.push({
          order: order++,
          status: tc.$.status,
          description: tc.$.name,
          expected_result: tc.$.name
        });

        let fileName = tc.$.name;
        if (tc.$.status == "FAIL") {
          classStatus = false;	
          if (fileMap.has(fileName)) {
            let newValue = fileMap.get(fileName) + 1;
            fileMap.set(fileName, newValue);
            fileName += `_${newValue}`
          } else {
            fileMap.set(fileName, 1);
          }
          attachments.push({
            name: `${fileName}.txt`,
            data: Buffer.from(tc.exception['full-stacktrace']).toString("base64"),
            content_type: "text/plain"
          });
        }
      }
    }

    if (MAX_LOG_FILE < attachments.length) {
      //let tmpFolder = tmp.dirSync();
      let tmpFolder = fs.mkdtempSync(path.join(os.homedir(), "junit-parser"));
      for (let a of attachments) {
        fs.writeFileSync(path.join(tmpFolder, a.name), Buffer.from(a.data, "base64"));
      }
      try {
        await zipFolder(tmpFolder, "*.txt", path.join(`${tmpFolder}`, `${obj.$.name}.zip`));
        attachments = [{
          name: `${obj.$.name}.zip`,
          data: fs.readFileSync(path.join(`${tmpFolder}`, `${obj.$.name}.zip`), {
            encoding: "base64"
          }),
          content_type: "application/zip"

        }];
      } catch (ex) {
        console.error(ex);
      }

      deleteFolderSync(tmpFolder);
    }
    return {
      attachments,
      testStepLogs
    }
  };

  let name = obj.$.name;

  let exe_start_date = timestamp;
  let exe_end_date = timestamp;
  let testcases = Array.isArray(obj['test-method']) ? obj['test-method'] : [obj['test-method']];
  let attchment_steplogs = await buildAttachmentAndTestStepLogs(testcases);
  let status = (classStatus) ? "PASS" : "FAIL";
  let testCase = {
    status: status,
    name: name,
    attachments: attchment_steplogs.attachments,
    exe_start_date: exe_start_date.toISOString(),
    exe_end_date: exe_end_date.toISOString(),
    automation_content: name,
    test_step_logs: attchment_steplogs.testStepLogs, 
    module_names: [suite, name]
  };
  if (testCase && !resultMap.has(testCase.automation_content)) {
    testCase.order = order++;
    resultMap.set(testCase.automation_content, testCase);
  }
}


function parseFile(fileName) {
  return new Promise((resolve, reject) => {
    let jsonString = fs.readFileSync(fileName, "utf-8");
    xml2js.parseString(jsonString, {
      preserveChildrenOrder: true,
      explicitArray: false,
      explicitChildren: false
    }, function (err, result) {
      if (err) {
        reject(err);
      } else {
        resolve(result);
      }
    });
  });
}

async function parse(pathToTestResult, useClassNameAsTestCaseName) {
  if (!fs.existsSync(pathToTestResult)) {
    throw new Error(`Test result not found at ${pathToTestResult}`);
  }
  useTestCase = useClassNameAsTestCaseName
  console.log("Path to test result: " + pathToTestResult);
  let resultFiles = [];
  if (fs.statSync(pathToTestResult).isFile()) {
    resultFiles.push(pathToTestResult);
  }
  if (fs.statSync(pathToTestResult).isDirectory()) {
    let pattern = undefined;
    pathToTestResult = pathToTestResult.replace(/\\/g, "/");
    if (pathToTestResult[pathToTestResult.length - 1] === '/') {
      pattern = pathToTestResult + "**/*.xml";
    } else {
      pattern = pathToTestResult + "/**/*.xml";
    }
    resultFiles = globby.sync(pattern);
  }
  if (0 === resultFiles.length) {
    throw new Error(`Could not find any result log-file(s) in: ' + pathToTestResult`);
  }
  for (let file of resultFiles) {
    console.log(`Parsing ${file} ...`);
    let parseFileResult = undefined;
    try {
      parseFileResult = await parseFile(file);
    } catch (error) {
      console.error(`Could not parse ${file}`, error);
      continue;
    }
    let testSuites = Array.isArray(parseFileResult['testng-results'].suite.test) ? parseFileResult['testng-results'].suite.test : [parseFileResult['testng-results'].suite.test];
    for (let ts of testSuites) {
      await buildResultsBySuite(ts);
    }
    console.log(`Finish parsing ${file}`);
  }
  return (Array.from(resultMap.values()));
};

module.exports = {
  parse: parse
};