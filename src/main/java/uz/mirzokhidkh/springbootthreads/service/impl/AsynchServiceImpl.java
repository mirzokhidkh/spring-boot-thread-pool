package uz.mirzokhidkh.springbootthreads.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import uz.mirzokhidkh.springbootthreads.component.MyRunnableTask;
import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.service.AsynchService;
import uz.mirzokhidkh.springbootthreads.repository.ClientDAO;

import java.util.List;

@Service
public class AsynchServiceImpl implements AsynchService {

    private final ClientDAO clientDao;

    //    @Autowired
    private final ApplicationContext appContext;

//    @Autowired

    private final TaskExecutor taskExecutor;

    public AsynchServiceImpl(
            ClientDAO clientDao,
            ApplicationContext appContext,
            @Qualifier("threadPoolTaskExecutor") TaskExecutor taskExecutor
    ) {
        this.clientDao = clientDao;
        this.appContext = appContext;
        this.taskExecutor = taskExecutor;
    }


    public void executeAsynchronously(Integer balance) {

        List<Integer> allIDs = clientDao.getAllIntegers();
        System.out.println("DATA SIZE = " + allIDs.size());
//        List<List<Integer>> lists = Lists.partition(allIDs, 500);

        allIDs.forEach(id -> {
            ClientBalanceDTO clientBalanceDTO = new ClientBalanceDTO(id, balance);
            MyRunnableTask task = new MyRunnableTask(clientBalanceDTO, clientDao);
            taskExecutor.execute(task);
        });



//        MyRunnableTask myRunnableTask = appContext/**/.getBean(MyRunnableTask.class);


//        taskExecutor.execute(myRunnableTask);

    }


}
