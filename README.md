# CoderDojoManagementSystem
コーダードージョー管理システムとして、使用できれば良いと思い。作成しました。
現段階では以下の機能があるだけです。

## インストール方法(Windowsの場合)
次の操作を行ってください。[Mavenのインストール方法に関してはこちらに記載](https://zenryokuservice.com/wp/2023/07/15/maven%e3%82%92%e3%81%a1%e3%82%83%e3%82%93%e3%81%a8%e5%ad%a6%e7%bf%92%e3%81%99%e3%82%8b/)

1. Javaのインストール
2. Mavenのインストール
3. Gitのインストール
4. 環境変数の設定
5. gitリポジトリのクローン
コマンドプロンプトで「doms」フォルダを作成して、移動、そしてクローンを行います。
```
mkdir doms
cd ./doms
git https://github.com/ZenryokuService/CoderDojoManagementSystem.git
```
6. Maven Installの実行
```
mvn install
```
4. Maven execの実行
```
mvn exec:java
```

## インストール方法(Linuxの場合)
次に示すコマンドを実行してください。
1. Javaのインストール
```
$ sudo apt update
$ sudo apt install openjdk-17-jdk
```
2. Mavenのインストール
```
sudo apt install maven 
```
3. gitのインストール
```
sudo add-apt-repository ppa:git-core/ppa
apt install git
```
3. gitリポジトリのクローン
「doms」フォルダを作成して、移動、そしてクローンを行います。
```
mkdir doms
cd ./doms
git https://github.com/ZenryokuService/CoderDojoManagementSystem.git
```

3. Maven Installの実行
```
mvn install
```
4. Maven execの実行
```
mvn exec:java
```
   
## 機能一覧
・「００００」を手入力することで管理者ログインができます。ニンジャ登録などにご使用ください。
・１３桁バーコードを入力すると自動でログイン、未登録のバーコードの場合は登録画面が表示

## ユースケース

1. アプリを起動する。
2. 名札(バーコード付き)をアプリに「ピッ」ってやる
3. ログインして「今日のやること」を入力
4. 同じ日付で２回目以降のログインでは「今日の結果」を登録する

## 今後
この機能で事足りるかどうかわからないので、使用してみることにします。

## 画面の変更について
他、SceneBuilderを使用して画面の作成、編集が可能です。
※resources/*.fxml

## 表示内容の変更について
resources/setting.propertiesを編集することで値を変更することができます。
