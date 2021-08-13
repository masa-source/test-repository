/**
 *
 */
const timerList = document.querySelectorAll('.timer');
timerList.forEach((timer) => {
	// タイマーの時間（分）を取得
	const parentNode = timer.parentNode;
	const previousElementChild = parentNode.previousElementSibling;
	let timeMin= parseInt(previousElementChild.innerHTML);

	// ミリ秒に変換
	let timeMSec = timeMin*60*100;

	// 初期表示を設定
	timer.innerHTML = timeFormatter(timeMSec);

	//ボタンがクリックされたときの処理
	const timerBtn = timer.previousElementSibling;
	timerBtn.onclick = function() {
		const timerId = setInterval(() => {
			timeMSec = timeMSec - 100;
			let label = timeFormatter(timeMSec);
			if(timeMSec <= 0) {
				clearInterval(timerId);
				label = '終了';
				alert(label);
			}
			timer.innerHTML = label;
		}, 1000);
	};

});

// ”〇〇:〇〇:〇〇”の形式に変換する関数
function timeFormatter (num){
	const Hr = addZeroPadding(Math.floor(num/ (60*60*100)));
	const Min = addZeroPadding(Math.floor((num/ (60*100)) % 60));
	const Sec = addZeroPadding(Math.floor((num/ (100)) % 60));
	let str = `${Min}:${Sec}`
	if(Hr !== '00') str = `${Hr}:` + str;

	return str;
}

// 二桁の数字にする関数
function addZeroPadding(num) {
	return String (num).padStart(2, '0');
}