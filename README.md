

1. git clone git@github.com:hanada31/ICCBot.git
2. cd ICCBot
3. # Initialize soot-dev submodule
git submodule update --init soot-dev

# Use -DskipTests to skip tests of soot (make build faster)
mvn -f pom.xml clean package -DskipTests

# Copy jar to root directory
cp target/ICCBot-jar-with-dependencies.jar /home/jiakun/Projects/TinyApp/libs/icc.jar