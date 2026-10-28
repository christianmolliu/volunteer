FROM mcr.microsoft.com/devcontainers/java:1-21-bookworm

USER root

# Disable the Yarn repository with the invalid signing key
RUN rm -f /etc/apt/sources.list.d/yarn.list
