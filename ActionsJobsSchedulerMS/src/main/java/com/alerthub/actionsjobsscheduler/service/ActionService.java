package com.alerthub.actionsjobsscheduler.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alerthub.actionsjobsscheduler.dto.ActionCreateRequest;
import com.alerthub.actionsjobsscheduler.dto.ActionResponse;
import com.alerthub.actionsjobsscheduler.model.Action;
import com.alerthub.actionsjobsscheduler.repository.ActionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionService {
	private final ActionRepository actionRepository;

    @Transactional
    public ActionResponse createAction(ActionCreateRequest request) {
        Action action = new Action();
        action.setUserId(request.getUserId());
        action.setName(request.getName());
        action.setCondition(request.getCondition());
        action.setTo(request.getTo());
        action.setActionType(request.getActionType());
        action.setRunOnTime(request.getRunOnTime());
        action.setRunOnDay(request.getRunOnDay());
        action.setMessage(request.getMessage());
        action.setCreateDate(LocalDateTime.now());
        action.setEnabled(true);
        action.setDeleted(false);
        action.setLastUpdate(LocalDateTime.now());

        Action saved = actionRepository.save(action);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ActionResponse> getAllActions() {
        return actionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ActionResponse getById(UUID id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));
        return toResponse(action);
    }

    private ActionResponse toResponse(Action a) {
        return ActionResponse.builder()
                .id(a.getId())
                .userId(a.getUserId())
                .name(a.getName())
                .condition(a.getCondition())
                .to(a.getTo())
                .actionType(a.getActionType())
                .runOnTime(a.getRunOnTime())
                .runOnDay(a.getRunOnDay())
                .message(a.getMessage())
                .enabled(a.isEnabled())
                .deleted(a.isDeleted())
                .createDate(a.getCreateDate())
                .lastUpdate(a.getLastUpdate())
                .lastRun(a.getLastRun())
                .build();
    }
}
