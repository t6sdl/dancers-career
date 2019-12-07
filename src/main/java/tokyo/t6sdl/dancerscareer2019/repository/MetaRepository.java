package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;

public interface MetaRepository {
	List<String> tables();
	List<String> columns(String table);
}
