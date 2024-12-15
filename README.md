# code-generater
确保私钥文件的正确性
确保你的私钥是以 PKCS#8 格式生成的。
使用以下命令重新生成私钥：
bash
复制代码
# 生成私钥（PKCS#8 格式）
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048

# 生成对应的公钥
openssl rsa -pubout -in private_key.pem -out public_key.pem