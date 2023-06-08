// 아이디 실시간 중복 점검
function checkUsername() {
	const username = document.getElementById("username").value;

	if (username !== "") {
		$
				.ajax({
					url : "/user/check-username",
					type : "GET",
					data : {
						username : username
					},
					success : function(result) {
						// 아이디가 중복되지 않은 경우
						document.getElementById("username-check").innerHTML = "<span style='color: green;'>사용가능한 아이디입니다.</span>";
					},
					error : function(xhr, textStatus, error) {
						// 아이디가 중복된 경우
						document.getElementById("username-check").innerHTML = "<span style='color: red;'>이미 사용 중인 아이디입니다.</span>";
					}
				});
	} else {
		document.getElementById("username-check").innerHTML = "";
	}
}

// 이메일 실시간 중복 점검
function checkEmail() {
	const email = document.getElementById("email").value;

	if (email !== "") {
		$
				.ajax({
					url : "/user/check-email",
					type : "GET",
					data : {
						email : email
					},
					success : function(result) {
						// 이메일이 중복되지 않은 경우
						document.getElementById("email-check").innerHTML = "<span style='color: green;'>사용 가능한 이메일입니다.</span>";
					},
					error : function(xhr, textStatus, error) {
						// 이메일이 중복된 경우
						document.getElementById("email-check").innerHTML = "<span style='color: red;'>이미 사용 중인 이메일입니다.</span>";
					}
				});
	} else {
		document.getElementById("email-check").innerHTML = "";
	}
}

// 닉네임 중복 점검
function checkNickname() {
	const nickname = document.getElementById("nickname").value;

	if (nickname !== "") {
		$
				.ajax({
					url : "/user/check-nickname",
					type : "GET",
					data : {
						nickname : nickname
					},
					success : function(result) {
						// 아이디가 중복되지 않은 경우
						document.getElementById("nickname-check").innerHTML = "<span style='color: green;'>사용 가능한 닉네임입니다.</span>";
					},
					error : function(xhr, textStatus, error) {
						// 아이디가 중복된 경우
						document.getElementById("nickname-check").innerHTML = "<span style='color: red;'>이미 사용 중인 닉네임입니다.</span>";
					}
				});
	} else {
		document.getElementById("nickname-check").innerHTML = "";
	}
}

// 핸드폰번호 중복 점검
function checkMobile() {
	const mobile = document.getElementById("mobile").value;

	if (mobile !== "") {
		$
				.ajax({
					url : "/user/check-mobile",
					type : "GET",
					data : {
						mobile : mobile
					},
					success : function(result) {
						if (result === true) { // 사용 가능한 경우
							document.getElementById("mobile-check").innerHTML = "<span style='color: green;'>사용 가능한 핸드폰번호입니다.</span>";
						} else { // 이미 사용 중인 경우
							document.getElementById("mobile-check").innerHTML = "<span style='color: red;'>이미 사용 중인 핸드폰번호입니다.</span>";
						}
					},
					error : function(xhr, textStatus, error) {
						console.log(xhr.statusText);
					}
				});
	} else {
		document.getElementById("mobile-check").innerHTML = "";
	}
}

// 도로명 주소 검색
function getPostcodeAddress() {
	new daum.Postcode({
		oncomplete : function(data) {
			// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

			// 각 주소의 노출 규칙에 따라 주소를 조합한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var fullAddr = ''; // 최종 주소 변수(도로명 주소)
			var fullAddrJibun = ''; // 최종 주소 변수(지번 주소)
			var extraAddr = ''; // 조합형 주소 변수

			// //////////////////////////////////////////////////////////////

			console.log("도로명 주소 : " + data.roadAddress);
			console.log("지번 주소 : " + data.jibunAddress);
			console.log("지번 주소(자동처리 : 지번 미출력시 자동 입력처리) : "
					+ data.autoJibunAddress);

			// javateacher) 이 부분을 생략하여 도로명과 지번이 같이 넘어가도록 조치

			// 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
			/*
			 * if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
			 * 
			 * fullAddr = data.roadAddress; } else { // 사용자가 지번 주소를 선택했을 경우(J) //
			 * fullAddr = data.jibunAddress; fullAddrJibun = data.jibunAddress; }
			 */

			fullAddr = data.roadAddress;
			// 지번 미입력시 : 자동 입력 지번 주소 활용(data.autoJibunAddress)
			fullAddrJibun = data.jibunAddress == '' ? data.autoJibunAddress
					: data.jibunAddress;

			// 사용자가 선택한 주소가 도로명 타입일때 조합한다.
			// if(data.userSelectedType === 'R'){

			// 법정동명이 있을 경우 추가한다.
			if (data.bname !== '') {
				extraAddr += data.bname;
			}
			// 건물명이 있을 경우 추가한다.
			if (data.buildingName !== '') {
				extraAddr += (extraAddr !== '' ? ', ' + data.buildingName
						: data.buildingName);
			}

			// 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
			// fullAddr += (extraAddr !== '' ? ' ('+ extraAddr
			// +')' : '');
			fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
			// fullAddrJibun += (extraAddr !== '' ? ' ('+
			// extraAddr +')' : ''); // javateacher 추가
			// }

			// javateacher end)

			// //////////////////////////////////////////////////////////////

			// 주소 정보 전체 필드 및 내용 확인 : javateacher
			var output = '';
			for ( var key in data) {
				output += key + ":" + data[key] + "\n";
			}

			console.log("-----------------------------")
			console.log(output);
			console.log("-----------------------------")

			// 3단계 : 해당 필드들에 정보 입력
			// 우편번호와 주소 정보를 해당 필드에 넣는다.

			document.getElementById('zip').value = data.zonecode; // 5자리
			// 새우편번호
			// 사용
			// document.getElementById('address1').value =
			// fullAddr;

			document.getElementById('address1').value = fullAddr; // 도로명
			// 주소

			// javateacher) 지번 주소 별도 할당
			// document.getElementById('address2').value =
			// fullAddrJibun; // 지번 주소

			// 커서를 상세주소 필드로 이동한다.
			document.getElementById('address2').focus();
		}
	}).open();
}