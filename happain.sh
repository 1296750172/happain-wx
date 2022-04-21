port=$(lsof -i:80 -t)
echo $port
if [  $port ]; then
  kill -9 $port
  echo $port
fi
nohup java -jar *.jar > app.log 2>&1 &
