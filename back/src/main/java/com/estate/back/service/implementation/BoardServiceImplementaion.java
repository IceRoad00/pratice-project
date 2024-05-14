package com.estate.back.service.implementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import com.estate.back.dto.request.board.PostBoardRequestDto;
import com.estate.back.dto.request.board.PostCommentRequestDto;
import com.estate.back.dto.request.board.PutBoardRequestDto;
import com.estate.back.dto.response.ResponseDto;
import com.estate.back.dto.response.board.GetBoardListResponseDto;
import com.estate.back.dto.response.board.GetBoardResponseDto;
import com.estate.back.dto.response.board.GetSearchBoardListResponseDto;
import com.estate.back.entitiy.BoardEntity;
import com.estate.back.repository.BoardRepository;
import com.estate.back.repository.UserRepository;
import com.estate.back.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplementaion implements BoardService{
    
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    
    @Override
    public ResponseEntity<ResponseDto> postBoard(PostBoardRequestDto dto, String userId) {

        try {
            
            boolean isExistUser = userRepository.existsByUserId(userId);
            if(!isExistUser) return ResponseDto.authenticationFailed();
            
            BoardEntity boardEntity = new BoardEntity(dto, userId);
            boardRepository.save(boardEntity);
            
            
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }
    
    @Override
    public ResponseEntity<ResponseDto> postComment(PostCommentRequestDto dto, int receptionNumber) {
        
        try {

            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();
            boolean status = boardEntity.getStatus();
            if(status) return ResponseDto.writtenComment();

            String comment = dto.getComment();
            boardEntity.setStatus(true);
            boardEntity.setComment(comment);

            boardRepository.save(boardEntity);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getBoardList() {
        
        try {
            // SELECT*FROM board ORDER BY reception_number DESC;
            // findByOrderByReceptionNumberDesc();

            List<BoardEntity> boardEntities = boardRepository.findByOrderByReceptionNumberDesc();            
            return GetBoardListResponseDto.success(boardEntities);
            
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

    }

    @Override
    public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(String searchWord) {
        
        try {

            // SELECT*from board WHERE title LIKE '%searchWord%' ORDER BY reception_number DESC;
            // findByTitleContainsOrderByReceptionNumberDesc();
            // Contains / Containing / IsContaining = LIKE '%word%'
            // StartingWith = LIKE 'word%'
            // EndingWith = LIKE '%word'

            List<BoardEntity> boardEntities = boardRepository.findByTitleContainsOrderByReceptionNumberDesc(searchWord);  
            
            return GetSearchBoardListResponseDto.success(boardEntities);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetBoardResponseDto> getBoard(int receptionNumber) {

        try {

            // - 데이터베이스의 Board테이블에서 receptionNumber에 해당하는 레코드 조회
            // SELECT * FROM board WHERE reception_number = :receptionNumber;

            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);

            // 데이터 유효성 검사 / 존재하지 않으면 noExistBoard 반환
            if(boardEntity == null) return ResponseDto.noExistBoard();

            return GetBoardResponseDto.success(boardEntity);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<ResponseDto> increaseViewCount(int receptionNumber) {

        try {

            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);

            // 데이터 유효성 검사 / 존재하지 않으면 noExistBoard 반환
            if(boardEntity == null) return ResponseDto.noExistBoard();
            
            // 존재하면 조회수 1 증가시키고 boardEntity를 save 처리 (데이터베이스에 저장)
            boardEntity.increaseViewCount();
            boardRepository.save(boardEntity);
            
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<ResponseDto> deleteBoard(int receptionNumber, String userId) {
        
        try {
            // SELECT ~ entity / boolean

            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();
            
            String writerId = boardEntity.getWriterId();
            boolean isWriter = userId.equals(writerId);
            if(!isWriter) return ResponseDto.authorizationFailed();

            boardRepository.delete(boardEntity);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<ResponseDto> putBoard(PutBoardRequestDto dto, int receptionNumber, String userId) {
        
        try {

            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();

            String writerId = boardEntity.getWriterId();
            boolean isWriter = userId.equals(writerId);
            if(!isWriter) return ResponseDto.authorizationFailed();

            boolean status = boardEntity.getStatus();
            if(status) return ResponseDto.writtenComment();

            // boardEntity에서 한번에 불러올 수 있게 작성
            boardEntity.update(dto);
            boardRepository.save(boardEntity);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }



    
}
