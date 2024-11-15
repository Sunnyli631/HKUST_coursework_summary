cd classes
sudo chmod 777 SearchEngine.db
sudo chmod 777 PageTree.db
sudo chmod 777 WordIndexer.db
java -classpath ".:lib/org/htmlparser/util/htmlparser.jar:lib/jdbm-1.0.jar" comp4321/Main
cd ..
cp -r classes/stopwords.txt .
cp -r classes/WordIndexer.db .
cp -r classes/WordIndexer.lg .
cp -r classes/PageTree.db .
cp -r classes/PageTree.lg .
cp -r classes/SearchEngine.lg .
cp -r classes/SearchEngine.db .
sudo chmod 777 SearchEngine.db
sudo chmod 777 PageTree.db
sudo chmod 777 WordIndexer.db