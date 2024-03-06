package util;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.util.function.Consumer;

public class TransactionUtil {
    private static final ThreadLocal<Transaction> transaction = new ThreadLocal<>();
    public void doInTransaction(Consumer<Session> session) {
        if(transaction.get() != null && transaction.get().is)
            return transaction.get();
        try {
            transaction = session.beginTransaction();
            transactionalCode.run(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error in transaction", e);
        } finally {
            session.close();
        }
    }

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
        }
    }

    private static void beginCurrentTransaction() {
        if(transaction.get() != null)
            transaction.get().begin();
    }

    private static boolean isTransacaoAberta() {
        return transaction.get() != null && transaction.get().isActive();
    }

    private static Session getCurrentSession() {
        return SessionUtil.getSession();
    }
}
