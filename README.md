[![Travis Build Status](https://travis-ci.org/java-park-mail-ru/samples.svg?branch=master)](https://travis-ci.org/java-park-mail-ru/samples)
[![CircleCI Build Status](https://circleci.com/gh/java-park-mail-ru/samples/tree/master.svg?style=svg)](https://circleci.com/gh/java-park-mail-ru/samples/tree/master)

Это репозиторий с примерами кода к лекциям. Каждый пример в нём независим от остальных и то, что они объединены в один большой и многомодульный можно игнорировать. Если вы скопируете папку с любым примером себе в отдельное место - он тоже будет работать. К некоторым лекциям дано несколько примеров - каждый из них тоже независим от всех других.

Краткий обзор файлов и примеров:
- [.gitignore](.gitignore) - лучше взять это, чем пару страниц мусора из случайного поиска в интернета
- [.mvn](.mvn) [mvnw](mvnw) [mvnw.bat](mvnw.bat) - скрипт для запуска Maven, избавляющий от необходимости ставить его в систему. Можно копировать и пользоваться, а все команды заменять с mvn something на ./mvnw something
- [.travis.yml](.travis.yml) - конфигурация для Travis. Можно использовать, как основу для своей конфигурации к РК2 (лекция 2-1 и 2-2).
- [.circleci/config.yml](.circleci/config.yml) - конфигурация для CircleCi. Альтернатива Travis-у.
- [pom.xml](pom.xml) и другие с `<packaging>pom</packaging>` - конфиги мавена для сборки и проверки независимых примеров за один раз. Можно спокойно игнорировать.

Презентации лекций:
- [1.1](https://yadi.sk/i/aqJhx9l63SYscs)
- [1.2](https://yadi.sk/d/T0jSRAmS3SizHQ)
- [2.1](https://cloud.mail.ru/public/DDCF/kpeVANK7q)
- [2.2](https://cloud.mail.ru/public/41uQ/QrRmnUifn)
- [2.3](https://cloud.mail.ru/public/DCJz/wvyGMH2AM)

Если вы нашли ошибку в любом примере, или считаете что его можно улучшить - открывайте PR или issue.
