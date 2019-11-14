# rest-assured-api-spark-sonar-demo [API Tests]


## Dependencies
Make sure you have installed on your operating system:<br/>
* [Java](http://www.java.com/) 
* [Git](https://git-scm.com/)
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/)


## Execution
The following steps should get you set up for running tests locally on your machine:

a). Clone this repository to your local machine: `git clone ssh://git@bitbucket.mckesson.com:7999/~e6fpk5u/rest-assured-api-spark-demo-sonar.git`<br/>
b). All commands should be run from the `\rest-assured-api-spark-sonar-demo` directory cloned during setup process above.<br/>


## Stages
1. Components testing. Runtime WebService (Jetty - 9.4.12.v20180830 - http://localhost:7779)

		a) Partially covered - $ mvn clean site -DtNG=Component -Pjacoco-ut
		b) Fully covered - $ mvn clean site -DtNG=Component_Full -Pjacoco-ut
		c) Save [ target/jacoco.exec ] - components code coverage


2. Run WebContainer (Tomcat - 9.0.27 - http://localhost:7778) with VM arguments for jacoco agent

		a) Setup [CATALINA_OPTS="$CATALINA_OPTS -javaagent:~/.m2/repository/org/jacoco/org.jacoco.agent/0.8.1/org.jacoco.agent-0.8.1-runtime.jar=destfile=~/WorkSpace/rest-assured-api-spark-demo-sonar/jacoco-it/jacoco-it.exec,includes=*,append=false,output=file"]
		b) macOS: $ Tomcat/bin/startup.sh
		c) http://localhost:7778/manager/html

3. Deploy (war)

		a) $ mvn -Ptc_l tomcat7:undeploy && mvn clean site -DtNG=Component_Full -Ptc_l tomcat7:deploy

4. Integration testing
	
		a) http://localhost:7778/manager/html
		b) Full - $ mvn clean site -DtNG=Component_Full -Duri=localhost/v1 -Dport=7778

5. Stop WebContainer
	
		a) macOS: $ Tomcat/bin/shutdown.sh
		b) Save [ jacoco-it/jacoco-it.exec ] - integration code coverage

6. Generate Java Code Coverage report - Using jacoco maven plugin
	
		a) $ mvn jacoco:report-integration -Pjacoco-ut (OR $ mvn org.jacoco:jacoco-maven-plugin:0.8.5:report-integration -Pjacoco-ut)

7. Generate Java Code Coverage report - Using SonarQube

		a) SonarQube V=7.9.1
			1) $ docker run -d --name sonarqube -p 9091:9000 sonarqube:latest (OR $ docker start sonarqube)
			2) $ docker ps
			3) $ mvn sonar:sonar -Dsonar.username=admin -Dsonar.password=admin -Dsonar.host.url=http://127.0.0.1:9091

		b) SonarQube V=5.6.7 
			1) $ docker run -d --name sonarqube_old -p 9092:9000 sonarqube:5.6.7-alpine (OR $ docker start sonarqube_old)
			2) $ docker ps
			3) $ mvn sonar:sonar -Dsonar.username=admin -Dsonar.password=admin -Dsonar.host.url=http://127.0.0.1:9092
			
		c) Stop Docker - $ docker stop sonarqube sonarqube_old (docker start sonarqube sonarqube_old)


### Static Analysis

*	`mvn clean -Pstatic-analysis site -Dmaven.test.skip=true`


# Reports
In project exist 5 kinds of reports:

* [TestNG](http://testng.org/doc/documentation-main.html) produces ‘index.html‘ report and it resides in the same test-output folder. This report gives the link to all the different components of the TestNG report like Groups & Reporter Output.

* [SureFire](http://maven.apache.org/surefire/maven-surefire-plugin/) report. The Surefire Plugin is used during the test phase of the build lifecycle to execute the unit tests of an application.

* [Allure](http://allure.qatools.ru/) report. An open-source framework designed to create test execution reports clear to everyone in the team. 

* [JaCoCo](https://www.eclemma.org/) report. The JaCoCo provides the JaCoCo runtime agent to your tests and allows basic report creation. 

* [SonarQube](https://www.sonarqube.org/) service. SonarQube is an open-source platform developed by SonarSource for continuous inspection of code quality to perform automatic reviews with static analysis of code to detect bugs, code smells, and security vulnerabilities. 
