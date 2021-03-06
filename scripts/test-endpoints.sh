#!/bin/sh

#TODO grep ServiceEndpoint from serverless info -v

getEndpoint () {
  echo $(serverless info -v | grep ServiceEndpoint | cut -d ' ' -f 2)
}

BASEURL=$(getEndpoint)

echo "Testing API endpoints in $BASEURL"

echo "---------------------------------------------"
echo "| Basic tests: All should return sane value |"
echo "---------------------------------------------"
for i in hello hello-json vanilla-js ; do
  echo GETing "$i"
  curl "$BASEURL/$i"
  echo
done

for i in '?op=-&value1=11&value2=2' '?op=*&value1=11&value2=2' ; do
  echo GETing calc with "$i"
  curl "$BASEURL/calc$i"
  echo
done

echo "------------------------------------------------------------------------"
echo "| FAILING test. Returns internal servers errors or other failure codes |"
echo "------------------------------------------------------------------------"
for i in fail-hard ; do
  echo GETing "$i"
  curl "$BASEURL/$i"
  echo
done

for i in '?aa=bb' '?op=hax' '' ; do
  echo GETing calc with "$i"
  curl "$BASEURL/calc$i"
  echo
done
