# Copyright 2016 Emmanuel Hugonnet (c) 2016 Red Hat, inc..
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
FROM jboss/wildfly:latest
MAINTAINER "Emmanuel Hugonnet" <ehsavoie@apache.org>

USER root
# Copy contents
RUN mkdir /opt/jboss/wildfly/binaries
COPY src/main/script/script.cli /opt/jboss/wildfly/binaries
COPY target/snoopee-service-2.0.0-SNAPSHOT.war /opt/jboss/wildfly/binaries

#Configure Server
RUN echo 'embed-server --admin-only --server-config=standalone-full.xml' > /opt/jboss/wildfly/bin/.jbossclirc
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/jboss/wildfly/binaries/script.cli
#restore the default cli configuration
RUN sed -i '$ d' /opt/jboss/wildfly/bin/.jbossclirc
RUN rm -Rf /opt/jboss/wildfly/binaries
#Create admin user 
RUN /opt/jboss/wildfly/bin/add-user.sh -u admin -p docker#admin --silent

# Fix for Error: Could not rename /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history

#For openshift
RUN chown -R 1001:0 /opt/jboss/wildfly && chown -R 1001:0 $HOME && chmod -R ug+rw /opt/jboss/wildfly
USER 1001

#For Docker
#RUN chown -R jboss:jboss /opt/jboss/wildfly/
#USER jboss

# Expose the ports we're interested in
EXPOSE 8080 9990

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to external interface
CMD /opt/jboss/wildfly/bin/standalone.sh -b `hostname -i` -bmanagement `hostname -i` -c standalone-full.xml 
