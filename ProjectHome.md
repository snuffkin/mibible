# 更新情報 #
0.3.0betaをリリースしました。MIB検索機能が強化されています。ただし、制限事項としてSNMP電文の送受信はできません。0.3.0正式リリースではSNMP電文の送受信も可能にします。

## | **次期バージョンのベータ版ダウンロードはここからお願いします：** http://mibible.googlecode.com/files/mibible-0.3.0beta.zip |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------| ##
## | **最新正式版ダウンロードはここからお願いします：** http://mibible.googlecode.com/files/mibible-0.2.0.zip | ##

年内には、0.3.0正式版をリリースしたいな…

# 概要 #
このプロジェクトではSNMPに関するツール群を開発していきます。基本的には、「SNMPに関して、あると助かるツール群を提供する」をコンセプトとして開発していきたいと考えています。

[mibble](http://www.mibble.org/)を中心に据えているため、現在はGPLライセンスとなっていますが、将来の目標としてはApache Licenseのような自由さのあるライセンスにしたいと考えています。

このツールの具体的な利用方法については、http://code.google.com/p/mibible/w/listを参照してください。

**mibible browser(ver0.3.0beta)の画面イメージ**

![http://groups.google.co.jp/group/mibible/web/mibible-browser-0.3.0beta.jpg](http://groups.google.co.jp/group/mibible/web/mibible-browser-0.3.0beta.jpg)

**mibible printer(ver0.2.0)の実行例**
```
> mibprinter "%type:%Num %text" ..\mib\ietf\IF-MIB
OBJECT IDENTIFIER:1 iso
OBJECT IDENTIFIER:1.3 org
OBJECT IDENTIFIER:1.3.6 dod
OBJECT IDENTIFIER:1.3.6.1 internet
OBJECT IDENTIFIER:1.3.6.1.1 directory
OBJECT IDENTIFIER:1.3.6.1.2 mgmt
OBJECT IDENTIFIER:1.3.6.1.2.1 mib-2
(中略)
OBJECT-TYPE:1.3.6.1.2.1.2.2.1 ifEntry
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.1 ifIndex
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.2 ifDescr
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.3 ifType
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.4 ifMtu
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.5 ifSpeed
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.6 ifPhysAddress
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.7 ifAdminStatus
OBJECT-TYPE:1.3.6.1.2.1.2.2.1.8 ifOperStatus
(中略)
NOTIFICATION-TYPE:1.3.6.1.6.3.1.1.5.3 linkDown
NOTIFICATION-TYPE:1.3.6.1.6.3.1.1.5.4 linkUp
(後略)
```

# 名前の由来 #
名前の由来は「mib + bible」です。SNMPの世界で聖書のように多くの方々に利用されるプロダクトを目指した、大それた名前です^^;　また、mibbleをリスペクトしつつも、ユーザや開発者に対する愛(i)を中心に据えたい、との想いもあります。「ミバイブル」ではなく、ネイティブっぽく「ミバイボ」と発音してください。

# 目標 #
以下のようなものを開発していきたいと考えています(2008年10月現在)。
変わる可能性は大いにあります。
  * 素敵なMIBブラウザ(mibible browser)
  * 素敵なMIB加工ツール(mibible printer)
  * 素敵なSNMP Agent
  * net-snmpのようなコマンド群

# メンバ募集中 #
このプロダクトを一緒に成長させて頂けるメンバを募集中です。実際にソースを書くだけでなく、動作させて貴重な意見を頂けたり、ドキュメントを書いて頂けたり、HPをメンテナンスして頂けたり、その人なりの様々な関わり方ができると考えています。

mibbleがJavaであるため基本的にはJavaで開発しています。その他言語を利用したい場合はご相談ください。Javaに固執するつもりはありませんが、様々な環境に配布しやすいプロダクトにしたいと考えています。

ご興味がある方は![http://groups.google.co.jp/group/mibible/web/tknstyk-mail.png](http://groups.google.co.jp/group/mibible/web/tknstyk-mail.png)までご連絡ください。また、[Googleグループのmibibleページ](http://groups.google.co.jp/group/mibible)もありますので、必要に応じて利用しようと考えています。

# バグや要望の提出先 #
バグや要望については、是非![http://groups.google.co.jp/group/mibible/web/tknstyk-mail.png](http://groups.google.co.jp/group/mibible/web/tknstyk-mail.png)までご連絡をお願いします。

# 沿革 #

| 2008/10/18 | mibibleプロジェクト発足。メンバ1名(tknstyk) |
|:-----------|:---------------------------------------------------------|
| 2008/10/20 | mibible-0.1.0 リリース(mibible browserリリース) |
| 2008/10/22 | メンバ新規加入(bluemoonredsky) |
| 2008/10/23 | メンバ新規加入(markon.0827) |
| 2008/10/23 | mibible-0.1.1 暫定リリース |
| 2008/10/25 | mibible-0.2.0 リリース([mibible printer](mibibleprinter.md)リリース) |
| 2008/11/25 | mibible-0.3.0beta リリース |