cp -r lib classes
cp src/stopwords.txt classes
export CLSPATH="classes:classes/lib/org/htmlparser/util/htmlparser.jar:classes/lib/jdbm-1.0.jar"
export PKGPATH="src/comp4321/"
javac -d classes lib/Porter.java
javac -d classes src/comp4321/FileOutput.java
javac -d classes src/comp4321/FileInput.java
javac -d classes -classpath $CLSPATH src/comp4321/PageKeyword.java
javac -d classes -classpath $CLSPATH src/comp4321/PageScore.java
javac -d classes -classpath $CLSPATH src/comp4321/WordInverted.java
javac -d classes -classpath $CLSPATH src/comp4321/OutPrintItem.java
javac -d classes -classpath $CLSPATH src/comp4321/StringPreprocessor.java
javac -d classes -classpath $CLSPATH src/comp4321/Indexer.java
javac -d classes -classpath $CLSPATH src/comp4321/WebPage.java
javac -d classes -classpath $CLSPATH src/comp4321/PageTree.java
javac -d classes -classpath $CLSPATH src/comp4321/Spider.java
javac -d classes -classpath $CLSPATH src/comp4321/Query.java
javac -d classes -classpath $CLSPATH src/comp4321/SearchEngine.java
javac -d classes -classpath $CLSPATH src/comp4321/Main.java