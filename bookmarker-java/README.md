# bookmarker-java

Encrypt properties: `./mvnw jasypt:encrypt-value -Djasypt.encryptor.password="pwd" -Djasypt.plugin.value="plain-text"`

Decrypt properties: `./mvnw jasypt:decrypt-value -Djasypt.encryptor.password="pwd" -Djasypt.plugin.value="encrypted-text"`
