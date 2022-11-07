package uz.mirzokhidkh.springbootthreads.service;

import uz.mirzokhidkh.springbootthreads.domain.Client;
import uz.mirzokhidkh.springbootthreads.payload.ApiResponse;
import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientStateDTO;

import java.util.List;

public interface AsynchService {

    public void executeAsynchronously(Integer balance);
}
