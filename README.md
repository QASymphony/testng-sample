# testng-sample
This project is to demonstrate how to create TestNG for Java agent in [qTest Automation Host](https://support.qasymphony.com/hc/en-us/sections/115001595246-qTest-Automation-Host-2-0-User-Guide). It includes sample java code built with TestNG framework. 

You can compile this source code with Maven or Ant.

### How to integrate this project with qTest Automation Host
1. Download this TestNG project and unzip in your directory (eg: D:\Demo\testng-sample).
2. Open command line on Windows or Termincal on Linux/Mac. Go to directory D:\Demo\testng-sample and execute command: mvn clean compile package test
3. Set up new agent  Agent below:
4. [Download and install qTest Automation Host](https://support.qasymphony.com/hc/en-us/articles/115005243923-Download-qTest-Automation-Agent-Host). Then follow [this article](https://support.qasymphony.com/hc/en-us/articles/115005562026-Create-TestNG-for-Java-Agent) to create a TestNG for Java agent.