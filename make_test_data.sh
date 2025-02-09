curl -XPOST "localhost:18010/contracts/candidates/1?careGiverId=3"
curl -XPOST "localhost:18010/contracts/candidates/1?careGiverId=5"
curl -XPOST "localhost:18010/contracts/candidates/1?careGiverId=7"
curl -XPOST "localhost:18010/contracts/candidates/1?careGiverId=9"
curl -XPOST "localhost:18010/contracts/candidates/1?careGiverId=11"
curl -XPOST "localhost:18010/contracts/candidates/1?careGiverId=13"

MemberDetailsDto(id=1, careGiverCandidates=[MemberDto(id=5, name=dummyName5, profile=null, nickName=dummyNickName5), MemberDto(id=7, name=dummyName7, profile=null, nickName=dummyNickName7), MemberDto(id=3, name=dummyName3, profile=null, nickName=dummyNickName3), MemberDto(id=9, name=dummyName9, profile=null, nickName=dummyNickName9), MemberDto(id=13, name=dummyName13, profile=null, nickName=dummyNickName13), MemberDto(id=11, name=dummyName11, profile=null, nickName=dummyNickName11)], chatRooms=[ChatRoomDto(id=1, lastMessage=1221, members=[MemberDto(id=1, name=dummyName1, profile=null, nickName=dummyNickName1)], chatMessages=[ChatMessageDto(type=CHAT, roomId=1, senderId=1, content=1221, createdAt=2025-02-09T03:10:29.757392)])]))