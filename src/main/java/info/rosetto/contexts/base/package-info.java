/**
 * Rosettoが保持する基本的なゲーム状態を扱うクラスを含むパッケージ.<br>
 * ゲームそのものの状態を保持するWholeSpaceと、それ以外のシステムの状態を表すSystemContextからなり、
 * それらにはContextsクラスをファサードとしてアクセスするようになっている.<br>
 * セーブデータに保持するようなゲーム情報はWholeSpaceに、
 * システムで一時的に用いるような情報はSystemContextにすべてまとめて保持できるように設計する.
 */
package info.rosetto.contexts.base;