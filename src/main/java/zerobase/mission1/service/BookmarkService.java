package zerobase.mission1.service;

import zerobase.mission1.dto.BookmarkDTO;
import zerobase.mission1.dto.BookmarkGroupDTO;
import zerobase.mission1.entity.BookmarkGroup;
import zerobase.mission1.repository.BookmarkRepository;

import java.util.ArrayList;

public class BookmarkService {
    BookmarkRepository bookmarkRepository = new BookmarkRepository();

    public boolean addBookmarkGroup(String name, int sequence) {
        return bookmarkRepository.createBookmarkGroup(name, sequence);
    }

    public ArrayList<BookmarkGroup> getBookmarkGroups() {
        return bookmarkRepository.getBookmarkGroupList();
    }

    public boolean addBookmark(int id, String mgrNo) {
        return bookmarkRepository.createBookmark(id, mgrNo);
    }

    public ArrayList<BookmarkDTO> getBookmarks() {
        return bookmarkRepository.getBookmarkList();
    }

    public BookmarkDTO getBookmarkInformation(int id) {
        return bookmarkRepository.getBookmarkInfo(id);
    }

    public BookmarkGroupDTO getBookmarkGroupInformation(int id) {
        return bookmarkRepository.getBookmarkGroupInfo(id);
    }

    public boolean editBookmarkGroup(String bookmarkGroupName, int bookmarkGroupSeq, int id) {
        return bookmarkRepository.updateBookmarkGroup(bookmarkGroupName, bookmarkGroupSeq, id);
    }

    public boolean removeBookmarkGroup(int id) {
        return bookmarkRepository.deleteBookmarkGroup(id);
    }

    public boolean removeBookmark(int id) {
        return bookmarkRepository.deleteBookmark(id);
    }

}
