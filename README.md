1) Clone the repository on IntelliJ and load maven project.
2) Update the path of your chromedriver version (130) in EntrataTest.java file (make sure the chromedriver version is compatible with chrome browser version)
   ->System.setProperty("webdriver.chrome.driver", "path/of/chromedriver.exe");
4) Open Command Prompt and write cd/(path of the entrara-priyanka project)
5) Run the tests using the command: mvn verify or direct run EntrataTest.java file from IntelliJ IDE.
