mkfifo -m 600  mypipe
netcat -l -p 8081 < mypipe | tee >(cat 1>&2) | netcat localhost 8080 | tee >(cat 1>&2) | cat > mypipe 
