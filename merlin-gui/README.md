merlin-gui
==============

To run merlin via command line must use this settings:

Main class: 
es.uvigo.ei.aibench.Launcher

Program arguments: 
plugins_bin

VM arguments: 
-Djava.util.Arrays.useLegacyMergeSort=true
-Xss512M
-Xmx1G (at least)
-XX:+HeapDumpOnOutOfMemoryError
-Djavax.xml.accessExternalDTD=all

The database_settings.conf and sereval other configuration files must be created in conf!