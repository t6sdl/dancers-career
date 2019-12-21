# Migration
[Flyway](https://flywaydb.org/documentation/plugins/springboot)を使用
## ファイル
`src/main/resources/db/migration/`に配置。  
ファイル名は`V<VERSION>__<NAME>.sql`とする。  
マイグレーションバージョンは`_`で区切る。マイグレーション名に`_`は使わない。  
例えば、マイグレーションバージョンが`1.0.14`で、マイグレーション名が`create-table`の場合は、`V1_0_14__create-table.sql`というファイル名にする。
## 書き方
下記2つのいずれかの記述を用いる。

```sql
SET AUTOCOMMIT = 0;
BEGIN;

-- SQL statements to be executed

COMMIT; -- replace with 'ROLLBACK' if script is NOT complete yet
SET AUTOCOMMIT = 1;
```
```sql
START TRANSACTION;

-- SQL statements to be executed

COMMIT; -- replace with 'ROLLBACK' if script is NOT complete yet
```
この記述を忘れてmigrationを実行すると、migrationが失敗した場合 __途中までmigrationが反映__ され修正が非常に厄介になるため注意。
## 実行
Spring BootでRunすれば自動的にmigrationが実行される。  
flyway\_schema\_historyテーブルに実行結果が追加される。成功すれば`success: 1`となり、失敗すれば`success: 0`となる。
## 失敗した場合
flyway\_schema\_historyテーブルの該当行を削除し、Spring BootでRunすれば再度migrationが実行される。flyway\_schema\_historyテーブルの該当行を削除せずにmigrationを実行しようとするとエラーとなる。