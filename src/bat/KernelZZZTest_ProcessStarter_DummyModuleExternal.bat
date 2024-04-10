REM Der Pfad zum aktuell verwendeten Workspace wird als Argument mitgegeben, damit die Batch auf verschiedenen Maschinen laufen kann.
java -cp %1\bat\JAZModuleExternalDummy.jar debug.zBasic.util.moduleExternal.process.createDummy.ProcessCreateMockDummyMainZZZ
pause