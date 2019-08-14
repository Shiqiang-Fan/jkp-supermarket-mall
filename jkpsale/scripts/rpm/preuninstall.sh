#!/bin/sh
if [ "$1" = 0 ];then
  service $RPM_PACKAGE_NAME stop
  rm -rf /etc/init.d/$RPM_PACKAGE_NAME
  rm -rf /opt/$RPM_PACKAGE_NAME/$RPM_PACKAGE_NAME.jar
  rm -rf /opt/$RPM_PACKAGE_NAME
  echo "卸载完成"
else
  echo "更新完成"
fi
