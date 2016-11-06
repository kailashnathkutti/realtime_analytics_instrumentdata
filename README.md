# realtime_analytics_instrumentdata
Simple example to demonstrate rule engine rune time integration with real time. This time it is Drools with SpringXD
### Create a groovy project and import this source code. If you are working with KIE 6.4RC or above, the code should run as it is
### Create a stream stream
      create --name groovyprocessortest --definition "http --port=8008 | script --script=urgentsample_analytics.groovy  | log" --deploy
      Real business logic is in the rule engine and remaining logic can be added in the groovy script. One has to think well before making all business logic into rules
### To simulate realtime ingestion, following script may help
   cat <<folder>//datafile.txt | sed -e "s/^/curl -d \'POST HTTP\/1.1 localhost Content-Type:text\/plain Content-Length:10 text=/"|sed "s/$/\' http:\/\/localhost:8008/" | sh

If you need help, ping me kailashnath.kutti@gmail.com
