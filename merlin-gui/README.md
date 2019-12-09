merlin-aibench
==============

To run merlin via command line must use this settings:

Main class: 
pt.uminho.ceb.biosystems.merlin.gui.aibench.Launcher

Program arguments: 
plugins_bin

VM arguments: 
-Djava.util.Arrays.useLegacyMergeSort=true
-Xss512M
-Xmx5G
-XX:+HeapDumpOnOutOfMemoryError
-Djavax.xml.accessExternalDTD=all

The database_settings.conf must be created in conf, check the file content in pom.xml!