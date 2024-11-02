MERGE INTO users (user_id, name, email)
VALUES
    (11, 'testFirstUser', 'testFirstEmail'),
    (22, 'testSecondUser', 'testSecondEmail'),
    (33, 'testThirdUser', 'testThirdEmail'),
    (44, 'testFourthUser', 'testFourthEmail'),
    (55, 'testFifthUser', 'testFifthEmail');

MERGE INTO requests (request_id, description, created_at, owner_id)
VALUES
    (11, 'requestFirstDescription', null, 11),
    (22, 'requestSecondDescription', null, 22),
    (33, 'requestThirdDescription', null, 33),
    (44, 'requestFourthDescription', null, 44),
    (55, 'requestFifthDescription', null, 55);


MERGE INTO items (item_id, name, description, available, owner_id, request_id)
VALUES
    (11, 'testFirstItem', 'testFirstItemDescription', true, 11, 11),
    (22, 'testSecondItem', 'testSecondItemDescription', true, 11, 22),
    (33, 'testThirdItem', 'testThirdItemDescription', true, 33, 33),
    (44, 'testFourthItem', 'testFourthItemDescription', false, 44, null),
    (55, 'testFifthItem', 'testFifthItemDescription', false, 55, null);

MERGE INTO bookings (booking_id, start_date, end_date, item_id, booker_id, status)
VALUES
    (11, TIMESTAMP '2024-11-01 14:11:14.0', TIMESTAMP '2024-11-01 14:15:14.0', 11, 22, 'APPROVED'),
    (22, null, null, 22, 22, 'WAITING'),
    (33, null, null, 33, 22, 'WAITING'),
    (44, null, null, 44, 22, 'WAITING'),
    (55, null, null, 55, 22, 'WAITING'),
    (66, TIMESTAMP '2024-11-01 14:11:14.0', TIMESTAMP '2024-11-01 14:15:14.0', 11, 22, 'REJECTED'),
    (77, TIMESTAMP '2024-11-01 14:11:14.0', TIMESTAMP '2024-11-01 14:15:14.0', 11, 22, 'WAITING');


MERGE INTO comments (comment_id, text, item_id, author_id, created_at)
VALUES
    (11, 'testFirstCommentText', 11, 22, null),
    (22, 'testSecondCommentText', 11, 11, null),
    (33, 'testThirdCommentText', 11, 33, null),
    (44, 'testFourthCommentText', 44, 55, null),
    (55, 'testFifthCommentText', 55, 44, null);
















