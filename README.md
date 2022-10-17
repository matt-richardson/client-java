# Client java

> **DISCLAIMER**: We use Google Analytics for sending anonymous usage information such as agent's and client's names,
> and their versions after a successful launch start. This information might help us to improve both ReportPortal
> backend and client sides. It is used by the Report Portal team only and is not supposed for sharing with 3rd parties.

[![Maven Central](https://img.shields.io/maven-central/v/com.epam.reportportal/client-java.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.epam.reportportal%22%20AND%20a:%22client-java%22)
[![CI Build](https://github.com/reportportal/client-java/actions/workflows/ci.yml/badge.svg)](https://github.com/reportportal/client-java/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/reportportal/client-java/branch/develop/graph/badge.svg?token=IVTys0o4JT)](https://codecov.io/gh/reportportal/client-java)
[![Join Slack chat!](https://reportportal-slack-auto.herokuapp.com/badge.svg)](https://reportportal-slack-auto.herokuapp.com)
[![stackoverflow](https://img.shields.io/badge/reportportal-stackoverflow-orange.svg?style=flat)](http://stackoverflow.com/questions/tagged/reportportal)
[![Build with Love](https://img.shields.io/badge/build%20with-❤%EF%B8%8F%E2%80%8D-lightgrey.svg)](http://reportportal.io?style=flat)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## JVM-based clients configuration

### How to provide parameters

There are several ways to load parameters. Be aware that higher sources override lower ones. For example, properties
from file can be overridden by JVM variables.

| Order | Source                |
|-------|-----------------------|
| 1     | JVM arguments         |
| 2     | Environment variables |
| 3     | Properties file       |

### JVM arguments

Report Portal client does not necessarily need properties file to be configured. One of the option is to use JVM
arguments which have the highest priority among configuration ways. To use them you need to specify them in command line
after Java executable using `-D` flag.
Example:

```shell
$ java -Drp.endpoint=https://rp.epam.com/ -jar my-tests.jar
```

### Environment variables

In case of bypassing parameters through environment variables they should be specified in UPPERCASE separated by
underscores (`_`).
E.G.:

* `rp.endpoint` --> `RP_ENDPOINT`
* `rp.skipped.issue` --> `RP_SKIPPED_ISSUE`

### Property file

The most common way to start using an agent is to copy your configuration from UI of ReportPortal at User Profile
section or configure property file `reportportal.properties` in the following format:

```properties
rp.endpoint=https://rp.epam.com/
rp.api.key=8967de3b-fec7-47bb-9dbc-2aa4ceab8b1e
rp.launch=launch-name
rp.project=project-name
## OPTIONAL PARAMETERS
rp.reporting.async=true
rp.reporting.callback=true
rp.enable=true
rp.description=My awesome launch
rp.attributes=key:value;value
rp.rerun=true
rp.rerun.of=ae586912-841c-48de-9391-50720c35dd5a
rp.convertimage=true
rp.mode=DEFAULT
rp.skipped.issue=true
rp.batch.size.logs=20
rp.keystore.resource=<PATH_TO_YOUR_KEYSTORE>
rp.keystore.password=<PASSWORD_OF_YOUR_KEYSTORE>
```

For detailed parameter description see below sections.

Properties file should have `reportportal.properties` name. It can be situated on the class path (in the project
directory). If client can’t find the properties file it logs a warning.

## Parameters

### Common parameters

| **Property name**     | **Type** | **Description**                                                                                                                                                                                                                                                                                                                                                              | **Required** |
|-----------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| rp.endpoint           | String   | URL of web service, where requests should be send                                                                                                                                                                                                                                                                                                                            | Yes          |
| rp.api.key or rp.uuid | String   | Api token of user                                                                                                                                                                                                                                                                                                                                                            | Yes          |
| rp.launch             | String   | A unique name of Launch (Run). Based on that name a history of runs will be created for particular name                                                                                                                                                                                                                                                                      | Yes          |
| rp.project            | String   | Project name to identify scope                                                                                                                                                                                                                                                                                                                                               | Yes          |
| rp.launch.uuid        | String   | A unique Launch UUID to which the whole test execution will be uploaded. No new launch will be created if the property specified.                                                                                                                                                                                                                                            | No           |
| rp.enable             | Boolean  | Enable/Disable logging to Report Portal: rp.enable=true - enable log to RP server. Any other value means 'false': rp.enable=false - disable log to RP server. If parameter is absent in  properties file then automation project results will be posted on RP.                                                                                                               | No           |
| rp.description        | String   | Launch description                                                                                                                                                                                                                                                                                                                                                           | No           |
| rp.attributes         | String   | Set of attributes for specifying additional meta information for current launch. Format: key:value;value;build:12345-6. Attributes should be separated by “;”, keys and values - “:”.                                                                                                                                                                                        | No           |
| rp.reporting.async    | Boolean  | Enables asynchronous reporting. Available values - `true` or `false`(by default). Supported only in 5+ vesion                                                                                                                                                                                                                                                                | No           |
| rp.reporting.callback | Boolean  | Enables [callback reporting](https://github.com/reportportal/client-java/wiki/Callback-reporting-usefulness). Available values - `true` or `false`(by default). Supported only in 5+ vesion                                                                                                                                                                                  | No           |
| rp.rerun              | Boolean  | Enables [rerun mode](https://github.com/reportportal/documentation/blob/master/src/md/src/DevGuides/rerun.md). Available values - `true` or `false`(by default). Supported only in 5+ version                                                                                                                                                                                | No           |
| rp.rerun.of           | String   | Specifies UUID of launch that has to be reruned                                                                                                                                                                                                                                                                                                                              | No           |
| rp.convertimage       | Boolean  | Colored log images can be converted to grayscale for reducing image size. Values: ‘true’ – will be converted. Any other value means ‘false’.                                                                                                                                                                                                                                 | No           |
| rp.mode               | Enum     | ReportPortal provides possibility to specify visibility of executing launch. Currently two modes are supported: DEFAULT - all users from project can see this launch; DEBUG - all users except of Customer role can see this launch (in debug sub tab). Note: for all java based clients (TestNG, Junit) mode will be set automatically to "DEFAULT" if it is not specified. | No           |
| rp.skipped.issue      | Boolean  | ReportPortal provides feature to mark skipped tests as not 'To Investigate' items on WS side. Parameter could be equal boolean values: <li>`true` - skipped tests considered as issues and will be marked as 'To Investigate' on Report Portal. <li>`false` - skipped tests will not be marked as 'To Investigate' on application.                                           | No           |
| rp.batch.size.logs    | Integer  | Put logs into batches of specified size in order to rise up performance and reduce number of requests to server. Default = 10                                                                                                                                                                                                                                                | No           |
| rp.batch.payload.limit| Long     | Limit batches by payload size to avoid request rejection due to server limitations.                                                                                                                                                                                                                                                                                          | No           |
| rp.rx.buffer.size     | Integer  | Internal queue size for log processing, increase this value along with log batch size if you see not all your logs passing to server. Default = 128                                                                                                                                                                                                                          | No           |
| rp.keystore.resource  | String   | Put your JKS file into resources and specify path to it                                                                                                                                                                                                                                                                                                                      | No           |
| rp.keystore.password  | String   | Access password for JKS (certificate storage) package, mentioned above<br/>                                                                                                                                                                                                                                                                                                  | No           |

Launch name sets once before first execution, because in common launch parts are fixed for a long time. By keeping the
same launch name we will know a fixed list of suites behind it. That will allow us to have a history trend. On Report
Portal UI different launch iterations will be saved with postfix "\#number", like "Test Launch \#1", "Test Launch \#2"
etc.

> If mandatory parameters are missed client will log a warning and will be initialized in inactive state.

### Multi-process join parameters

| **Property name**                 | **Type** | **Description**                                                                                                                                                                                                                |
|-----------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| rp.client.join                    | Boolean  | Default: `true`<br /> Enable / Disable multi-process launch join mode                                                                                                                                                          |
| rp.client.join.mode               | Enum     | \[FILE, SOCKET], Default: `FILE`<br/> Which mechanism will be used to join multi-process launches:<br/> <li>`FILE` - the client will create a locking file<li>`SOCKET` - the client will open a socket                         |
| rp.client.join.port               | Integer  | Default: 25464<br>If client join mode set to `SOCKET`, this property controls port number of the socket                                                                                                                        |
| rp.client.join.timeout.value      | Integer  | Default: 1.8M milliseconds (30 minutes)<br> Timeout value for secondary launches. Primary launch will wait that amount of time after test execution for secondary launch finish.                                               |
| rp.client.join.timeout.unit       | Enum     | Default: `MILLISECONDS`<br> Timeout value time unit. Should be one of values from `java.util.concurrent.TimeUnit` class                                                                                                        |
| rp.client.join.file.lock.name     | String   | Default: `reportportal.lock`<br>A name of a main lock file, can be an absolute path. A client which managed to obtain that lock count itself as a primary launch process. It rewrites synchronization file with its launch ID. |
| rp.client.join.file.sync.name     | String   | Default: `reportportal.sync`<br>A name of a launch ID synchronization file, can be an absolute path. Each client waits for a lock on that file to get a launch ID (first line) and write its own ID to the end of the file.    |
| rp.client.join.lock.timeout.value | Integer  | Default: 1 minute<br> Files lock / connection timeout for launches.                                                                                                                                                            |
| rp.client.join.lock.timeout.unit  | Enum     | Default: `MILLISECONDS`<br> Timeout value time unit. Should be one of values from `java.util.concurrent.TimeUnit` class                                                                                                        |

### HTTP parameters

| **Property name**             | **Type** | **Description**                                                                                                                                                                                                                                          | 
|-------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| rp.http.proxy                 | String   | A URL of a HTTP proxy to connect to the endpoint.                                                                                                                                                                                                        |
| rp.http.logging               | Boolean  | Default: `false`<br> Enable / Disable HTTP logging.                                                                                                                                                                                                      |
| rp.http.timeout.call.value    | Integer  | Default: Infinitive<br> Timeout value for the entire call: resolving DNS, connecting, writing the request body, server processing, and reading the response body. If the call requires redirects or retries all must complete within one timeout period. |
| rp.http.timeout.call.unit     | Enum     | Default: `MILLISECONDS`<br> Timeout value time unit. Should be one of values from `java.util.concurrent.TimeUnit` class                                                                                                                                  |
| rp.http.timeout.connect.value | Integer  | Default: 10 seconds<br> Connect timeout for new HTTP connections.                                                                                                                                                                                        |
| rp.http.timeout.connect.unit  | Enum     | Default: `MILLISECONDS`<br> Timeout value time unit. Should be one of values from `java.util.concurrent.TimeUnit` class                                                                                                                                  |
| rp.http.timeout.read.value    | Integer  | Default: 10 seconds<br> Data read timeout for new HTTP connections.                                                                                                                                                                                      |
| rp.http.timeout.read.unit     | Enum     | Default: `MILLISECONDS`<br> Timeout value time unit. Should be one of values from `java.util.concurrent.TimeUnit` class                                                                                                                                  |
| rp.http.timeout.write.value   | Integer  | Default: 10 seconds<br> Data write timeout for new HTTP connections.                                                                                                                                                                                     |
| rp.http.timeout.write.unit    | Enum     | Default: `MILLISECONDS`<br> Timeout value time unit. Should be one of values from `java.util.concurrent.TimeUnit` class                                                                                                                                  |

## Proxy configuration

Report Portal supports 2 options for setting Proxy configuration:

* JVM arguments (-Dhttps.proxyHost=localhost)
* `reportportal.properties` file

### JVM arguments

Report Portal uses OkHttp as HTTP client, which can pick up JVM proxy settings. This is the most flexible and preferable
way to configure proxies, since it supports different proxy types. You can find out more about JVM proxies on
[Java networking and proxies](http://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html) page.

### Properties file

If you need to set up just a simple HTTP proxy you can use `reportportal.properties` file. `rp.http.proxy` parameter
accepts HTTP proxy URL. This parameter can override JVM proxy arguments, so watch your back.
