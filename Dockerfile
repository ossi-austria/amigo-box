FROM jangrewe/gitlab-ci-android

ARG GRADLE_VERSION=gradle-7.0.2 
RUN wget https://services.gradle.org/distributions/${GRADLE_VERSION}-all .zip
RUN mkdir -p .gradle/wrapper/dists/${GRADLE_VERSION}-all/unzipped
RUN unzip ${GRADLE_VERSION}.zip .gradle/wrapper/dists/${GRADLE_VERSION}-all/unzipped
RUN chmod +x .gradle/wrapper/dists/${GRADLE_VERSION}-all/unzipped/${GRADLE_VERSION}/bin/gradle

COPY .gradle/wrapper/dists ~/.gradle/wrapper/dists