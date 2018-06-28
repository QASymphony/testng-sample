const parser = require("../parser.js");

(async () => {

  try {
    console.log(`Parse JUnit report - test case name as method name`);
    var ret = await parser.parse("./tests/sample-testng-results/testng-results.xml");
    console.log(JSON.stringify(ret, null, 4))
  } catch (ex) {
    console.error(ex);
  }

})();