package baecon.devgames.connection.client.dto;

import baecon.devgames.model.Duplication;

/**
 * Created by Marcel on 09-5-2016.
 */
public class DuplicationDTO implements ModelDTO<Duplication> { // TODO: 09-5-2016  

    private long id;

    public DuplicationDTO(Duplication duplication) {
        this.id = duplication.getId();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Duplication toModel() {

        Duplication duplication = new Duplication();

        duplication.setId(id);

        return duplication;
    }
}
