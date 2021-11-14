INSERT INTO TSP_REQUEST (ID, HASH_ALGORITHM, HASH, NONCE, CERTIFICATE_REQUESTED, TSA_POLICY_ID, ASN_ENCODED)
VALUES (1, 'SHA512', 'U0hBNTEyLXRlc3QtMTIzNA==', 'A441F', 1, NULL, 'R0ZaVU9FQUlHRlBVWklFR1BJVUVBSEdQT0FTRUpIRw=='),
       (2, 'SHA256', 'U0hBMjU2LXRlc3QtNDE1', '1337', 1, NULL, 'Sk5PS0ZITkFFU09KSUdOQkVTQU/Dlg=='),
       (3, 'SHA1', 'U0hBMS10ZXN0LTUxNTE=', '3315', 0, '1.2.3.4', 'SFVJR0VBSFVJT0VTSFU='),
       (4, 'SHA256', 'U0hBMjU2LXRlc3QtNDIxNTIy', '515AB', 0, '1.53.32', 'SklMRkJHTldFQUlLQkZPSUE='),
       (5, 'SHA512', 'U0hBNTEyLXRlc3QtNDIxNTIy', '41FA', 1, '51.65.12', 'TlVPSUpGQUVISVVQR0hFU0dTSUVVR0lPU0U=');

INSERT INTO TSP_RESPONSE (ID, STATUS, STATUS_STRING, FAILURE_INFO, GENERATION_TIME, SERIAL_NUMBER, REQUEST_ID,
                          ASN_ENCODED)
VALUES (1, 0, 'OK', NULL, TIMESTAMP '2021-03-15 13:02:21', 'A13', 1, 'bT6yyIc8PaiD0X2kBNbR0+rqKQ=='),
       (2, 2, 'Bad Algorithm', 256, NULL, 'AF321', 2, 'dkTd+YpLsJOfOlElr4CqSM/zPg=='),
       (3, 0, 'OK', NULL, TIMESTAMP '2021-05-12 21:02:21', 'D4214', 3, 'Hpxmv3yyXwsla92bjtMf7miBKw=='),
       (4, 2, 'System Failure', 3411, NULL, '5325AC32', 4, 'Hpxmv3yyXwsla92bjtMf7miBKw=='),
       (5, 0, 'OK', NULL, TIMESTAMP '2021-09-23 17:32:12', 'FA241', 5, 'ZDtDCqv1QmdxoCFZlZ+4lY2bnQ==');

