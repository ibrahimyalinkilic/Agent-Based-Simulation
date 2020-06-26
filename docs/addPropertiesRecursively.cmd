@echo off
for /r . %%X in (*.html) do (
	svn propset svn:mime-type text/html "%%X"
)
