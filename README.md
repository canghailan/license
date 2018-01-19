# License生成工具

关键词：
* RSA签名
* 硬件检测
* JWT

## 1. RSA密钥对生成
```shell
java -jar license-1.0.0-jar-with-dependencies.jar kengen
# public.pem
# private.pem
```
生成X509公钥、PKCS8私钥，以pem格式保存。

## 2. 硬件检测
```shell
java -jar license-1.0.0-jar-with-dependencies.jar hardware
# hardware.json
```
基于[oshi](https://github.com/oshi/oshi)实现，读取CPU、主板序列号、MAC地址等硬件信息，保存为JSON格式。

## 3. License数据模板生成
读取```hardware.json```，生成License数据模板。
```shell
java -jar license-1.0.0-jar-with-dependencies.jar template
# license.yml
```
```yaml
# 授权自
iss: 
# 授予
sub: 
# 过期时间
exp: "2019-01-20 00:29:28"
# 签发时间
iat: "2018-01-20 00:29:28"
# CPU
hw-cpu: "BFBFBFBFBFBFBFBF"
# 主板序列号
hw-mobo: "161616161616161"
# MAC地址
hw-mac: "2c:2c:2c:2c:2c:2c"
```

## 4. License生成
完善```license.yml```，使用```private.pem```签名生成[JWT](https://github.com/jwtk/jjwt)格式license。
```shell
java -jar license-1.0.0-jar-with-dependencies.jar license
# license
```

## 5. License验证
通过```public.pem```验证license，并对License中的数据进行校验。
```shell
cc.whohow.license.License.License(key, license)
cc.whohow.license.License.check() // 验证CPU、主板序列号、MAC地址等
cc.whohow.license.License.check(rate) // 0.x概率进行验证
```
验证不通过，输出License信息，并通过```System.exit(-1)```退出程序。

## 依赖
* [oshi](https://github.com/oshi/oshi)
* [jjwt](https://github.com/jwtk/jjwt)
* [jcommander](https://github.com/cbeust/jcommander)

## 命令行说明
```shell
java -jar license-1.0.0-jar-with-dependencies.jar usage
```