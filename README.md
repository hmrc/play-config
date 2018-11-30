play-config
===========

[![Build Status](https://travis-ci.org/hmrc/play-config.svg)](https://travis-ci.org/hmrc/play-config) [ ![Download](https://api.bintray.com/packages/hmrc/releases/play-config/images/download.svg) ](https://bintray.com/hmrc/releases/play-config/_latestVersion)

A micro-library containing Play configuration utility functions.

### Not available for Play 2.6
This library has been inlined in `bootstrap-play-26` and shouldn't be used as a separate dependency for Play 2.6 services. It will not be updated for Play 2.6.

## Adding to your service

Include the following dependency in your SBT build

```scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies += "uk.gov.hmrc" %% "play-config" % "1.0.0"
```

