/**
 * Rosettoが保持する基本的なゲーム状態を扱うクラスを含むパッケージ.<br>
 * グローバル変数を保持するVariableContextと、関数を保持するFunctionContext、
 * それ以外のシステムの状態を表すSystemContextからなり、
 * それらにはContextsクラスをファサードとしてアクセスするようになっている.<br>
 * セーブデータに保持するようなゲーム情報はVariableContextに、
 * システム上で一時的に用いるような情報はSystemContextにすべてまとめて保持できるように設計する.
 */
package info.rosetto.contexts.base;