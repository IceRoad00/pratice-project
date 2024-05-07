package com.estate.back.common.object;

import com.estate.back.common.util.ChangeDateFormatUtil;
import com.estate.back.entitiy.BoardEntity;

import lombok.Getter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

@Getter
public class BoardListItem {

    private Integer receptionNumber;
    private Boolean status;
    private String title;
    private String writerId;
    private String writeDatetime;
    private Integer viewCount;

    private BoardListItem(BoardEntity boardEntity) throws Exception {
        // 객체를 writeDatetime으로 전달해줌
        String writeDatetime = ChangeDateFormatUtil.changeYYMMDD(boardEntity.getWriteDatetime());

        // boardEntity에서 writeId 가져옴
        String writerId = boardEntity.getWriterId();
        // writeId에서 첫 글자만 가져옴 -> 곱하기 반복해서 6글자면 5글자 *처리함
        writerId = writerId.substring(0, 1) + "*".repeat(writerId.length() - 1);

        this.receptionNumber = boardEntity.getReceptionNumber();
        this.status = boardEntity.getStatus();
        this.title = boardEntity.getTitle();
        this.writerId = writerId;
        this.writeDatetime = writeDatetime;
        this.viewCount = boardEntity.getViewCount();
    }

    // 데이터베이스에서 전체 리스트 조회 -> List<BoardEntity> -> List<BoardListItem>
    public static List<BoardListItem> getList(List<BoardEntity> boardEntities) throws Exception {
        // 빈 배열을 만듬
        List<BoardListItem> boardList = new ArrayList<>();

        // BoardEntity에 있는 속성들을 BoardListItem으로 한개씩 옮겨옴.
        for(BoardEntity boardEntity: boardEntities) {
            BoardListItem boardListItem = new BoardListItem(boardEntity);
            boardList.add(boardListItem);
        }

        return boardList;
    }
}
