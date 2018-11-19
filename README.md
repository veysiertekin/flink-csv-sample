# Flink Technical Case

## Pre-requests
ℹ️  Make sure docker and docker-compose are installed on the system, and docker daemon is running:

```bash
➜  docker info
Client:
 Version:      18.03.1-ce
 API version:  1.37
(...some other information...)

➜  docker-compose -v
docker-compose version 1.16.1, build 6d1ac21
```

## Building & Running the Application Stack

To build application artifact and custom apache-flink image, you need to build an image with a two staged `Dockerfile`. At first stage, application sources will be compiled and an `artifact` will be packaged. At second stage, a fresh custom apache-flink image will be backed.

```bash
➜  docker build . -t flink-case-image
```

When custom apache-flink image is ready, you may start a cluster combined  with a jobmanager and a taskmanager;

```bash
➜  docker-compose -p flink-case -f dc-flink-case.yml up -d --remove-orphans

Creating network "flink-case_flink-case-network" with driver "bridge"
Creating flink-case_jobmanager_1 ... done
Creating flink-case_taskmanager_1 ... done
```


* Custom image contains the `case.csv` file and the `artifact` have been builded.
* When Jobmanager is ready, a custom script will execute all the tasks mentioned in `technical-case` description **automatically** in **a single batch**, and will store all calculated outputs into `data/` dictionary.

```bash
➜  ls -la data/
total 96
drwxr-xr-x   8 vertekin  110224528    256 Nov 19 23:28 .
drwxr-xr-x  16 vertekin  110224528    512 Nov 19 23:42 ..
-rw-r--r--@  1 vertekin  110224528  28208 Nov 16 19:20 case.csv
-rw-r--r--   1 vertekin  110224528   3876 Nov 19 23:28 task-1_product-interactions.csv
-rw-r--r--   1 vertekin  110224528     38 Nov 19 23:28 task-2_event-counts.csv
-rw-r--r--   1 vertekin  110224528     14 Nov 19 23:28 task-3_top-five-users-fulfilled-all-events.csv
-rw-r--r--   1 vertekin  110224528     30 Nov 19 23:28 task-4_event-counts-of-user-47.csv
-rw-r--r--   1 vertekin  110224528     24 Nov 19 23:28 task-5_product-views-of-user-47.csv
```

After all tasks have been completed, application stack could be shutted down. You may need to look at the logs to be ensure all tasks completed:

```bash
➜  docker-compose -p flink-case -f dc-flink-case.yml logs -f
...
jobmanager_1   | 2018-11-19 20:55:12,245 INFO  org.apache.flink.runtime.jobmaster.JobManagerRunner           - JobManagerRunner already shutdown.
taskmanager_1  | 2018-11-19 20:55:12,274 INFO  org.apache.flink.runtime.taskexecutor.TaskExecutor            - Close JobManager connection for job e3677e663b28e82fbdf41cf9850c6291.
taskmanager_1  | 2018-11-19 20:55:12,275 INFO  org.apache.flink.runtime.taskexecutor.JobLeaderService        - Cannot reconnect to job e3677e663b28e82fbdf41cf9850c6291 because it is not registered.
jobmanager_1   | Program execution finished
jobmanager_1   | Job with JobID e3677e663b28e82fbdf41cf9850c6291 has finished.
jobmanager_1   | Job Runtime: 5906 ms
```

Then:

```bash
➜  docker-compose -p flink-case -f dc-flink-case.yml down -v
Stopping flink-case_taskmanager_1 ... done
Stopping flink-case_jobmanager_1  ... done
Removing flink-case_taskmanager_1 ... done
Removing flink-case_jobmanager_1  ... done
Removing network flink-case_flink-case-network
```

## Development Environment

Application have been written with scala, therefore you need `sbt` tool to do any changes.

`sbt` can be installed by following link: [https://www.scala-sbt.org/1.0/docs/Setup.html](https://www.scala-sbt.org/1.0/docs/Setup.html)

After installation of `sbt`, application can be started by following command at the project root:

```bash
➜  sbt "runMain org.bitbucket.veysiertekin.flinkcase.AnalyseTextFile --csvFile data/case.csv --outputPath data/"
[info] Loading settings for project global-plugins from idea.sbt ...
[info] Loading global plugins from /Users/vertekin/.sbt/1.0/plugins
[info] Loading settings for project flink-case-build from assembly.sbt ...
[info] Loading project definition from /Users/vertekin/playground/flink-case/project
[info] Loading settings for project root from idea.sbt,build.sbt ...
[info] Set current project to flink-case (in build file:/Users/vertekin/playground/flink-case/)
[info] Running (fork) org.bitbucket.veysiertekin.flinkcase.AnalyseTextFile --csvFile data/case.csv --outputPath data/
...
[success] Total time: 13 s, completed Nov 19, 2018, 11:48:11 PM

```

Also this command will execute all tasks directly and will produce a similar computing result (please ignore all warnings):

```bash
➜  ls -la data/                                                                                                 
total 64
drwxr-xr-x   8 vertekin  110224528    256 Nov 19 23:48 .
drwxr-xr-x  16 vertekin  110224528    512 Nov 19 23:48 ..
-rw-r--r--@  1 vertekin  110224528  28208 Nov 16 19:20 case.csv
drwxr-xr-x   6 vertekin  110224528    192 Nov 19 23:48 task-1_product-interactions.csv
drwxr-xr-x   6 vertekin  110224528    192 Nov 19 23:48 task-2_event-counts.csv
-rw-r--r--   1 vertekin  110224528     14 Nov 19 23:48 task-3_top-five-users-fulfilled-all-events.csv
drwxr-xr-x   6 vertekin  110224528    192 Nov 19 23:48 task-4_event-counts-of-user-47.csv
drwxr-xr-x   6 vertekin  110224528    192 Nov 19 23:48 task-5_product-views-of-user-47.csv
```


