import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class demo2 {

    @Test
    public void test1(){
        try {
            String brokerURL = "tcp://119.23.190.210:8161";
            /** 创建连接工厂 */
            ConnectionFactory connectionFactory =
                    new ActiveMQConnectionFactory(brokerURL);
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
             * */
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            /** 创建消息的目标(模式)队列 */
            Destination queue = session.createQueue("test-queue");
            /** 创建消息生产者 */
            MessageProducer producer = session.createProducer(queue);
            /** 创建文本消息 */
            TextMessage tm = session.createTextMessage();
            /** 设置消息内容 */
            tm.setText("您好，JMS我来了！");
            /** 发送消息到消息中间件 */
            producer.send(tm);
            System.out.println("==【生产者】已发送消息==");
            /** 关闭消息生产者、连接、会话 */
            producer.close();
            connection.close();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
        try {
            /** 定义连接消息中间件的地址(tcp协议) */
            String brokerURL = "tcp://119.23.190.210:8161";
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
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
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
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
