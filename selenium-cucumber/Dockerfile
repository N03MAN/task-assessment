# Use Ubuntu-based Selenium Chrome Debug image (supports Java installation)
FROM selenium/node-chrome-debug:latest

# Switch to root user to install packages
USER root

# Install OpenJDK 17 and Maven
RUN apt-get update && apt-get install -y openjdk-17-jdk maven && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Set Java environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Set working directory
WORKDIR /usr/src/app

# Copy project files into the container
COPY . .

# Expose ports for WebDriver & VNC viewer
EXPOSE 4444 5900

# Run the test suite
CMD ["mvn", "clean", "install"]
