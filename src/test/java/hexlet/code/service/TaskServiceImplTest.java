package hexlet.code.service;

import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskMappingHelper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskStatusRepository statusRepository;
    private UserRepository userRepository;
    private LabelRepository labelRepository;
    private TaskMapper taskMapper;
    private TaskMappingHelper taskMappingHelper;
    private TaskServiceImpl service;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        statusRepository = Mockito.mock(TaskStatusRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        labelRepository = Mockito.mock(LabelRepository.class);

        taskMapper = Mappers.getMapper(TaskMapper.class);
        taskMappingHelper = new TaskMappingHelper(statusRepository, userRepository, labelRepository);

        service = new TaskServiceImpl(
                taskRepository,
                statusRepository,
                taskMapper,
                taskMappingHelper
        );
    }

    @Test
    void dummy() {
    }
}
