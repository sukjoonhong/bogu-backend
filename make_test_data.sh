#!/bin/bash

# 1️⃣ RSA 공개 키 다운로드
PUBLIC_KEY_URL="http://localhost:18010/auth/public-key"
curl -s -X GET "$PUBLIC_KEY_URL" | jq -r '.publicKey' > public_key.pem

# 2️⃣ RSA 암호화 함수 정의
encrypt_password() {
  echo -n "$1" | openssl rsautl -encrypt -pubin -inkey public_key.pem | base64
}

# 3️⃣ 로그인 및 토큰 발급
AUTH_ID="dummyId1"
PASSWORD="dummyPassword1"
ENCRYPTED_PASSWORD=$(encrypt_password "$PASSWORD") # ✅ 비밀번호 암호화
LOGIN_URL="http://localhost:18010/auth/login"

# 로그인 요청 (POST 요청)
TOKEN=$(curl -s -X POST "$LOGIN_URL" \
     -H "Content-Type: application/json" \
     -d '{
           "id": 1,
           "authId": "'"$AUTH_ID"'",
           "password": "'"$ENCRYPTED_PASSWORD"'"
         }' | jq -r '.token')

rm -f public_key.pem
# 토큰 검증
if [ -z "$TOKEN" ] || [ "$TOKEN" == "null" ]; then
  echo "로그인 실패: 토큰을 가져올 수 없습니다."
  exit 1
fi

echo "✅ 로그인 성공! 발급된 토큰: $TOKEN"

# 4️⃣ 매칭 요청 (자동으로 Bearer Token 포함)
MATCHING_URL="http://localhost:18010/matching/requests/1"

for careGiverId in 3 5 7 9 11 13 15 17 19 20
do
  echo "⏳ 요청 중: $careGiverId"
  RESPONSE=$(curl -s -X POST "$MATCHING_URL?careGiverId=$careGiverId" \
       -H "Authorization: Bearer $TOKEN" \
       -H "Content-Type: application/json")

  echo "✅ 응답: $RESPONSE"
done

echo "🎉 모든 요청 완료!"
