import ResponseDto from "src/apis/response.dto";
import { BoardListItem } from "src/types";

// description : 게시물 검색 Response Body DTO
export interface GetBoardListResponseDto extends ResponseDto {
    boardList: BoardListItem[];
}

export interface GetSearchBoardListResponseDto extends ResponseDto {
    boardList: BoardListItem[];
}