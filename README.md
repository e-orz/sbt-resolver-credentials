# sbt-resolver-credentials
Typically, sbt associates resolvers with credentials based on the realm and host. However, there are cases where the same host and realm need different credentials, such as when GitLab uses distinct credentials for different groups of private repositories. This plugin enables you to override the default behavior and specify credentials for a particular resolver.

## Requirements
This plugin requires sbt 1.10.1+. Also, it overrides Coursier configuration so Coursier should be used for library management (It's the default, though. Just make sure not to change it).

## Usage
Add the following to your project/plugins.sbt file:
```sbt
addSbtPlugin("com.orzitzer" % "sbt-resolver-credentials" % "<latest version>")
```
Then add a map between resolver to its credentials. Use the `resolverCredentials` SettingKey (added by this plugin).

For example:
```sbt
resolvers += "My Repository" at "https://packages.confluent.io/maven/"
lazy val credentialOverrides = Map("My Repository" -> Credentials(...))
resolverCredentials ++= credentialOverrides
```
Both `DirectCredential` and `FileCredentials` can be used.

Note: the ID of the resolver and the ID in the overrides map must match ("My Repository" in the example).