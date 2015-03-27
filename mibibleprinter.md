# Introduction #

MIBファイルを整形して表示する。
整形するフォーマットは利用者が指定することができる。

# Details #

mibible/binで以下のコマンドを実行することで利用できる。
> mibprinter format file

このとき、formatの部分にキーワードを入れることで、MIB情報を見やすい形で表示できる。
利用できるキーワードは以下の通り。
  * %Text -> iso.org.dod.internet.mgmt.mib-2.interfaces.ifTable.ifEntry.ifOperStatus
  * %text -> ifOperStatus
  * %Num -> 1.3.6.1.2.1.2.2.1.8
  * %num -> 8
  * %TN -> iso(1).org(3).dod(6).internet(1).mgmt(2).mib-2(1).interfaces(2).ifTable(2).ifEntry(1).ifOperStatus(8)
  * %type -> OBJECT-TYPE
  * %access -> read-only

例)
mibprinter "%text:%Num(%type)" ..\mib\ietf\IF-MIB
を実行すると、以下のような形式でMIBツリーが表示される。
> ifOperStatus:1.3.6.1.2.1.2.2.1.8(OBJECT-TYPE)