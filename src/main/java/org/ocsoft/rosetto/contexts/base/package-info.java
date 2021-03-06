/**
 * Rosettoが保持する基本的なゲーム状態を扱うクラスを含むパッケージ.<br>
 * グローバル変数を保持するGlobalVariablesと、関数やマクロを保持するActionContext、
 * それ以外のシステムの状態を表すSystemContextからなり、
 * それらにはContextsクラスをファサードとしてアクセスするようになっている.<br>
 * セーブデータに保持するようなゲーム情報はGlobalVariablesに、
 * 実行しうる値の定義はActionContextに、
 * システム上で一時的に用いるような情報はSystemContextにすべてまとめて保持できるように設計する.
 */
package org.ocsoft.rosetto.contexts.base;