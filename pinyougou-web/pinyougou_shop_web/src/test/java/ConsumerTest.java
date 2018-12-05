import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsumerTest {
    public static void main(String[] args) throws Exception{
        /** 定义连接消息中间件的地址(tcp协议) */
        String brokerURL = "tcp://119.23.190.210:61616";
        /** 创建连接工厂 */
        ConnectionFactory connectionFactory = new
                ActiveMQConnectionFactory(brokerURL);
        /** 创建连接对象 */
        Connection connection = connectionFactory.createConnection();
        /** 开始连接 */
        connection.start();
        /**
         * 创建会话对象
         * 第一个参数transacted: 是否开启事务 true开启 false不开启
         * 第二个参数acknowledgeMode：应答模式
         * Session.AUTO_ACKNOWLEDGE: 自动应答
         * Session.CLIENT_ACKNOWLEDGE: 客户端应答
         * Session.DUPS_OK_ACKNOWLEDGE: 重复确认应答
         * Session.SESSION_TRANSACTED: 会话事务
         **/
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建消息的目标(模式)队列
        Destination queue = session.createQueue("test-queue");
        // 创建消息消费者
        MessageConsumer consumer = session.createConsumer(queue);
        // 设置消息监听器
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                // 判断消息的类型
                if (message instanceof TextMessage){
                    // 强制转换
                    TextMessage tm = (TextMessage)message;
                    // 获取消息内容
                    try {
                        System.out.println(tm.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
