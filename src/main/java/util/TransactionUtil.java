package util;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.util.function.Consumer;

public class TransactionUtil {
    private static final ThreadLocal<Transaction> transaction = new ThreadLocal<>();
    public static void doWithTransaction(Consumer<Session> command) {
        boolean isTransacaoFoiAbertaAqui = false;

        try(Session session = getCurrentSession()) {
            var sessionImpl = (SharedSessionContractImplementor) session;
            sessionImpl.setAutoClear(false);
            if (!TransactionUtil.isTransacaoAberta()) {

                TransactionUtil.beginCurrentTransaction();
                isTransacaoFoiAbertaAqui = true;
            }

            command.accept(session);

            if (isTransacaoFoiAbertaAqui) {
                TransactionUtil.commitCurrentTransaction();
            }

        } catch(Exception e) {
            TransactionUtil.rollbackCurrentTransaction();
            System.out.println(e.getMessage());
        }
    }

    private static void rollbackCurrentTransaction() {
        if(transaction.get() != null) transaction.get().rollback();
    }

    private static void commitCurrentTransaction() {
        if(transaction.get()!=null) transaction.get().commit();;
        transaction.set(null);
    }

    private static void beginCurrentTransaction() {
        if(transaction.get() == null) {
            transaction.set(getCurrentSession().getTransaction());
        }
        transaction.get().begin();
    }

    private static boolean isTransacaoAberta() {
        return transaction.get() != null;
    }

    private static Session getCurrentSession() {
        return SessionUtil.getSession();
    }
}
