package com.peklo.peklo.models.task_1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Tool1ItemRepository extends JpaRepository<Tool1Item, Long> {

    Iterable<Tool1Item> findAllByUserChatId(String chatId);

    @Query(value = "select distinct t.userChatId from Tool1Item as t ")
    Iterable<String> getAllUniqueChatIds();
}
