プログラミング応用Ⅲ・Ⅳ
=======

##概要
* 私の学校の科目であるプログラミング応用Ⅲ・Ⅳで開発しているソフトウェアのリポジトリです
* コードに関するご意見・ご感想はお気軽にどうぞ!

##実行環境(mt-bay)
* 統合開発環境：eclipse Luna
* コンパイラ：Java SE 1.7
* 参照ライブラリ：Slick2D, lwjgl

##チーム内向けに記述
* *仕様書にないオブジェクトを作成した場合は仕様書にどういう目的で作ったものかなどを書こう!*

###gitの使い方
* *わかりやすいコミットメッセージを心がけよう!*
* 基本はリポジトリをpull→コードを書いてcommit→githubにpushです!
* eclipseでプロジェクトとして使いたい場合：
    1. リポジトリのクローン
    2. 新規プロジェクトウィザードの使用を選択
    3. Javaプロジェクトをいい感じに作成(普通にプロジェクトを作る要領で大丈夫です)
    4. 「パッケージ・エクスプローラ」ビューでプロジェクトを選択して右クリック/プロパティーをクリック
    5. Java のビルド・パスをクリックして選択
    6. デフォルトのsrcフォルダがあったらいい感じに除去
    7. "ソース"タブを選択している状態でソースのリンクをクリック
    8. 1.で追加したリポジトリのソースファイルを参照して追加
    9. "ライブラリー"タブを選択している状態で「外部Jar追加」をクリック
    10. slick関係のjarファイルをビルドパスに追加(ライブラリのダウンロードなどは各自で行っていただけると助かります!)

* pullしたけど、追加されたファイルが見つからないぞ!って時(暫定。もっといい方法があれば是非教えてください。)
    1. プロジェクト・エクスプローラからsrcを削除
    2. プロジェクトを選択して右クリック/プロパティーを選択
    3. Java のビルド・パスを選択
    4. 先ほど削除したsrcフォルダと同一のディレクトリをリンク(eclipseでプロジェクトとして使いたい場合の
                                                             7.～8.参照)

###googleドライブを用いたファイル共有の際の注意点
* *ローカルで編集したファイルをアップロードする場合は、ほかの人の更新がないか確認しよう!*
* なるべくかぶらないファイル名にしよう!
